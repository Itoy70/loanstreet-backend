import axios, { AxiosInstance } from 'axios';

export interface Loan {
  id: string;
  amount: number;
  interestRate: number;
  lengthInMonths: number;
  monthlyPaymentAmount: number;
}

export type CreateLoanPayload = Omit<Loan, 'id'>;
export type UpdateLoanPayload = Omit<Loan, 'id'>;

export class LoanClient {
  private readonly http: AxiosInstance;

  constructor(baseURL: string = 'http://localhost:8080') {
    this.http = axios.create({
      baseURL,
      headers: { 'Content-Type': 'application/json' },
    });
  }

  async createLoan(data: CreateLoanPayload): Promise<Loan> {
    const response = await this.http.post<Loan>('/api/loans', data);
    return response.data;
  }

  async getLoan(id: string): Promise<Loan> {
    const response = await this.http.get<Loan>(`/api/loans/${id}`);
    return response.data;
  }

  async updateLoan(id: string, data: UpdateLoanPayload): Promise<Loan> {
    const response = await this.http.put<Loan>(`/api/loans/${id}`, data);
    return response.data;
  }
}
