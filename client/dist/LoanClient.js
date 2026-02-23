"use strict";
var __createBinding = (this && this.__createBinding) || (Object.create ? (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    var desc = Object.getOwnPropertyDescriptor(m, k);
    if (!desc || ("get" in desc ? !m.__esModule : desc.writable || desc.configurable)) {
      desc = { enumerable: true, get: function() { return m[k]; } };
    }
    Object.defineProperty(o, k2, desc);
}) : (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    o[k2] = m[k];
}));
var __setModuleDefault = (this && this.__setModuleDefault) || (Object.create ? (function(o, v) {
    Object.defineProperty(o, "default", { enumerable: true, value: v });
}) : function(o, v) {
    o["default"] = v;
});
var __importStar = (this && this.__importStar) || (function () {
    var ownKeys = function(o) {
        ownKeys = Object.getOwnPropertyNames || function (o) {
            var ar = [];
            for (var k in o) if (Object.prototype.hasOwnProperty.call(o, k)) ar[ar.length] = k;
            return ar;
        };
        return ownKeys(o);
    };
    return function (mod) {
        if (mod && mod.__esModule) return mod;
        var result = {};
        if (mod != null) for (var k = ownKeys(mod), i = 0; i < k.length; i++) if (k[i] !== "default") __createBinding(result, mod, k[i]);
        __setModuleDefault(result, mod);
        return result;
    };
})();
Object.defineProperty(exports, "__esModule", { value: true });
exports.LoanClient = exports.LoanApiError = exports.REQUEST_ID_HEADER = void 0;
const axios_1 = __importStar(require("axios"));
exports.REQUEST_ID_HEADER = 'X-Request-ID';
class LoanApiError extends Error {
    constructor(status, apiError, requestId) {
        super(apiError.error);
        this.status = status;
        this.apiError = apiError;
        this.requestId = requestId;
        this.name = 'LoanApiError';
    }
}
exports.LoanApiError = LoanApiError;
function extractRequestId(headers) {
    return headers[exports.REQUEST_ID_HEADER.toLowerCase()] ?? '';
}
function rethrowApiError(err) {
    if (err instanceof axios_1.AxiosError && err.response) {
        const { status, data, headers } = err.response;
        throw new LoanApiError(status, data, extractRequestId(headers));
    }
    throw err;
}
class LoanClient {
    constructor(baseURL = process.env.API_URL ?? 'http://localhost:8080') {
        this.http = axios_1.default.create({
            baseURL,
            headers: { 'Content-Type': 'application/json' },
        });
        this.http.interceptors.request.use((config) => {
            if (!config.headers[exports.REQUEST_ID_HEADER]) {
                config.headers[exports.REQUEST_ID_HEADER] = crypto.randomUUID();
            }
            return config;
        });
    }
    async createLoan(data, requestId) {
        try {
            const response = await this.http.post('/api/loans', data, {
                headers: requestId ? { [exports.REQUEST_ID_HEADER]: requestId } : {},
            });
            return { data: response.data, requestId: extractRequestId(response.headers) };
        }
        catch (err) {
            rethrowApiError(err);
        }
    }
    async getLoan(id, requestId) {
        try {
            const response = await this.http.get(`/api/loans/${id}`, {
                headers: requestId ? { [exports.REQUEST_ID_HEADER]: requestId } : {},
            });
            return { data: response.data, requestId: extractRequestId(response.headers) };
        }
        catch (err) {
            rethrowApiError(err);
        }
    }
    async updateLoan(id, data, requestId) {
        try {
            const response = await this.http.put(`/api/loans/${id}`, data, {
                headers: requestId ? { [exports.REQUEST_ID_HEADER]: requestId } : {},
            });
            return { data: response.data, requestId: extractRequestId(response.headers) };
        }
        catch (err) {
            rethrowApiError(err);
        }
    }
}
exports.LoanClient = LoanClient;
