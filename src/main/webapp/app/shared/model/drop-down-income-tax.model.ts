export interface IIncomeTaxDropDownMenu {
  id?: number;
  range?: string;
}
export class IncomeTaxDropDownMenu implements IIncomeTaxDropDownMenu {
  constructor(public id?: number, public range?: string) {}
}
