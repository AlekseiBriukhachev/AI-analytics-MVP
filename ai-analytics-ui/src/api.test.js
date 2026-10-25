import { apiRequest } from './api';

describe('apiRequest', () => {
    const originalFetch = global.fetch;

    beforeEach(() => {
        global.fetch = jest.fn();
    });

    afterEach(() => {
        global.fetch = originalFetch;
    });

    it('returns the JSON response body', async () => {
        global.fetch.mockResolvedValue({
            ok: true,
            status: 200,
            json: async () => ({ success: true, data: [] })
        });

        await expect(apiRequest('/api/models')).resolves.toEqual({ success: true, data: [] });
    });

    it('rejects unsuccessful API responses with the backend error', async () => {
        global.fetch.mockResolvedValue({
            ok: false,
            status: 400,
            json: async () => ({ success: false, error: 'Invalid dataset' })
        });

        await expect(apiRequest('/api/datasets')).rejects.toThrow('Invalid dataset');
    });
});
