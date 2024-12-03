import dayjs from 'dayjs/esm';

export interface ICertificateApprovalDto {
  referenceId?: string;
  signatoryPersonId?: number;
  issueDate?: dayjs.Dayjs;
  reason?: string;
}

export class CertificateApprovalDto implements ICertificateApprovalDto {
  constructor(public referenceId?: string, public signatoryPersonId?: number, public issueDate?: dayjs.Dayjs, public reason?: string) {}
}
