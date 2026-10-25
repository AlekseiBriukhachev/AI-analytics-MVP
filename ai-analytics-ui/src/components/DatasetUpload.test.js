import { parseCsv } from './DatasetUpload';

describe('parseCsv', () => {
    it('parses headed CSV into backend data points and feature names', () => {
        expect(parseCsv('price, quantity, label\r\n1.5, 2, 1\r\n0.5, 3, 0')).toEqual({
            headers: ['price', 'quantity', 'label'],
            data: [
                { features: [1.5, 2], label: 1 },
                { features: [0.5, 3], label: 0 }
            ]
        });
    });

    it('parses CSV without a header', () => {
        expect(parseCsv('1,2,1\n3,4,0')).toEqual({
            headers: null,
            data: [
                { features: [1, 2], label: 1 },
                { features: [3, 4], label: 0 }
            ]
        });
    });

    it('rejects rows with non-numeric values', () => {
        expect(() => parseCsv('feature,label\nnot-a-number,1')).toThrow('Row 2 must contain numeric features and a numeric label.');
    });
});
