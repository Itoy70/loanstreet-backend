"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.LoanClient = void 0;
const axios_1 = __importDefault(require("axios"));
class LoanClient {
    constructor(baseURL = 'http://localhost:8080') {
        this.http = axios_1.default.create({
            baseURL,
            headers: { 'Content-Type': 'application/json' },
        });
    }
    async createLoan(data) {
        const response = await this.http.post('/api/loans', data);
        return response.data;
    }
    async getLoan(id) {
        const response = await this.http.get(`/api/loans/${id}`);
        return response.data;
    }
    async updateLoan(id, data) {
        const response = await this.http.put(`/api/loans/${id}`, data);
        return response.data;
    }
}
exports.LoanClient = LoanClient;
