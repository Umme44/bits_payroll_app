import dayjs from 'dayjs/esm';
export interface IHoldFbDisbursementApproval {
  disbursedAt?: dayjs.Dayjs | null;
  remarks?: string;
  listOfId?: number[];
}

export class HoldFbDisbursementApproval implements IHoldFbDisbursementApproval {
  constructor(public disbursedAt?: dayjs.Dayjs | null, public remarks?: string, public listOfId?: number[]) {}
}
