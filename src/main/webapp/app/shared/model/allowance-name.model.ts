export interface IAllowanceName {
  allowance01Name?: String;
  allowance02Name?: String;
  allowance03Name?: String;
  allowance04Name?: String;
  allowance05Name?: String;
  allowance06Name?: String;
}

export class AllowanceName implements IAllowanceName {
  constructor(
    public allowance01Name?: String,
    public allowance02Name?: String,
    public allowance03Name?: String,
    public allowance04Name?: String,
    public allowance05Name?: String,
    public allowance06Name?: String
  ) {}
}
