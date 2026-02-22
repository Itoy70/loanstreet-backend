import axios, { AxiosError, AxiosInstance } from 'axios';

export const REQUEST_ID_HEADER = 'X-Request-ID';

export interface Loan {
  id: string;
  amount: number;
  interestRate: number;
  lengthInMonths: number;
  monthlyPaymentAmount: number;
}

export interface FieldViolation {
  field: string;
  message: string;
}

export interface ApiError {
  timestamp: string;
  error: string;
  errors?: FieldViolation[];
}

export class LoanApiError extends Error {
  constructor(
    public readonly status: number,
    public readonly apiError: ApiError,
    public readonly requestId: string,
  ) {
    super(apiError.error);
    this.name = 'LoanApiError';
  }
}

export interface LoanResponse<T = Loan> {
  data: T;
  requestId: string;
}

export type CreateLoanPayload = Omit<Loan, 'id'>;
export type UpdateLoanPayload = Omit<Loan, 'id'>;

function extractRequestId(headers: Record<string, unknown>): string {
  return (headers[REQUEST_ID_HEADER.toLowerCase()] as string) ?? '';
}

function rethrowApiError(err: unknown): never {
  if (err instanceof AxiosError && err.response) {
    const { status, data, headers } = err.response;
    throw new LoanApiError(status, data as ApiError, extractRequestId(headers));
  }
  throw err;
}

export class LoanClient {
  private readonly http: AxiosInstance;

  constructor(baseURL: string = process.env.API_URL ?? 'http://localhost:8080') {
    this.http = axios.create({
      baseURL,
      headers: { 'Content-Type': 'application/json' },
    });

    this.http.interceptors.request.use((config) => {
      if (!config.headers[REQUEST_ID_HEADER]) {
        config.headers[REQUEST_ID_HEADER] = crypto.randomUUID();
      }
      return config;
    });
  }

  async createLoan(data: CreateLoanPayload, requestId?: string): Promise<LoanResponse> {
    try {
      const response = await this.http.post<Loan>('/api/loans', data, {
        headers: requestId ? { [REQUEST_ID_HEADER]: requestId } : {},
      });
      return { data: response.data, requestId: extractRequestId(response.headers) };
    } catch (err) {
      rethrowApiError(err);
    }
  }

  async getLoan(id: string, requestId?: string): Promise<LoanResponse> {
    try {
      const response = await this.http.get<Loan>(`/api/loans/${id}`, {
        headers: requestId ? { [REQUEST_ID_HEADER]: requestId } : {},
      });
      return { data: response.data, requestId: extractRequestId(response.headers) };
    } catch (err) {
      rethrowApiError(err);
    }
  }

  async updateLoan(id: string, data: UpdateLoanPayload, requestId?: string): Promise<LoanResponse> {
    try {
      const response = await this.http.put<Loan>(`/api/loans/${id}`, data, {
        headers: requestId ? { [REQUEST_ID_HEADER]: requestId } : {},
      });
      return { data: response.data, requestId: extractRequestId(response.headers) };
    } catch (err) {
      rethrowApiError(err);
    }
  }
}
