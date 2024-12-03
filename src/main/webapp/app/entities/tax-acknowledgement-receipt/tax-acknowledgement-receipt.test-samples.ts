import dayjs from 'dayjs/esm';

import { AcknowledgementStatus } from 'app/entities/enumerations/acknowledgement-status.model';

import { ITaxAcknowledgementReceipt, NewTaxAcknowledgementReceipt } from './tax-acknowledgement-receipt.model';

export const sampleWithRequiredData: ITaxAcknowledgementReceipt = {
  id: 95227,
  tinNumber: 'Steel',
  receiptNumber: 'Toys',
  taxesCircle: 'Cambridgeshire',
  taxesZone: 'Arizona e-markets Cambridgeshire',
  dateOfSubmission: dayjs('2022-09-13'),
  filePath: '../fake-data/blob/hipster.txt',
  acknowledgementStatus: AcknowledgementStatus['RECEIVED'],
};

export const sampleWithPartialData: ITaxAcknowledgementReceipt = {
  id: 2613,
  tinNumber: 'scale parse invoice',
  receiptNumber: 'leading Incredible Rustic',
  taxesCircle: 'transmitting override',
  taxesZone: 'Gourde Officer',
  dateOfSubmission: dayjs('2022-09-12'),
  filePath: '../fake-data/blob/hipster.txt',
  acknowledgementStatus: AcknowledgementStatus['SUBMITTED'],
  updatedAt: dayjs('2022-09-13T06:47'),
};

export const sampleWithFullData: ITaxAcknowledgementReceipt = {
  id: 51275,
  tinNumber: 'Avon viral Clothing',
  receiptNumber: 'Refined mission-critical',
  taxesCircle: 'Games',
  taxesZone: 'RSS',
  dateOfSubmission: dayjs('2022-09-13'),
  filePath: '../fake-data/blob/hipster.txt',
  acknowledgementStatus: AcknowledgementStatus['RECEIVED'],
  receivedAt: dayjs('2022-09-12T22:30'),
  createdAt: dayjs('2022-09-13T02:41'),
  updatedAt: dayjs('2022-09-13T07:26'),
};

export const sampleWithNewData: NewTaxAcknowledgementReceipt = {
  tinNumber: 'Card Public-key',
  receiptNumber: 'parsing Wooden SAS',
  taxesCircle: 'directional',
  taxesZone: 'convergence',
  dateOfSubmission: dayjs('2022-09-12'),
  filePath: '../fake-data/blob/hipster.txt',
  acknowledgementStatus: AcknowledgementStatus['SUBMITTED'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
