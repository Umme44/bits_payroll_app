export interface IApprovalDTO {
  listOfIds?: number[];
}

export class ApprovalDTO implements IApprovalDTO {
  constructor(public listOfIds?: number[]) {}
}
