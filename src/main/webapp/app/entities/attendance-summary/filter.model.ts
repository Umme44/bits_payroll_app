export class AttendanceSummaryFilterDto {
  constructor(public text = '') {
    this.text = text;
  }

  deserialize(input: any): this {
    Object.assign(this, input);
    return this;
  }
}
