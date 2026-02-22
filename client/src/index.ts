import { LoanClient, LoanApiError } from './LoanClient';

async function main(): Promise<void> {
  const baseUrl = process.env.API_URL ?? 'http://localhost:8080';
  const client = new LoanClient(baseUrl);

  console.log('--- Creating loan ---');
  const created = await client.createLoan({
    amount: 250000,
    interestRate: 0.065,
    lengthInMonths: 360,
    monthlyPaymentAmount: 1580.17,
  });
  console.log('Created:', JSON.stringify(created, null, 2));

  const { id } = created.data;
  console.log(`\n--- Fetching loan ${id} ---`);
  const fetched = await client.getLoan(id);
  console.log('Fetched:', JSON.stringify(fetched, null, 2));

  console.log(`\n--- Updating loan ${id} ---`);
  const updated = await client.updateLoan(id, {
    amount: 200000,
    interestRate: 0.055,
    lengthInMonths: 360,
    monthlyPaymentAmount: 1264.14,
  });
  console.log('Updated:', JSON.stringify(updated, null, 2));
}

main().catch((err) => {
  if (err instanceof LoanApiError) {
    console.error(`API error [${err.status}]: ${err.message} (requestId: ${err.requestId})`);
    if (err.apiError.errors) {
      for (const v of err.apiError.errors) {
        console.error(`  - ${v.field}: ${v.message}`);
      }
    }
  } else {
    console.error('Error:', err.message);
  }
  process.exit(1);
});
