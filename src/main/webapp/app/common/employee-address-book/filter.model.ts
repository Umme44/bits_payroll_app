import dayjs from 'dayjs/esm';

export class Filter {
  constructor(
    public destinationId = 0,
    public departmentId = 0,
    public unitId = 0,
    public searchText = '',
    public bloodGroup = '',
    public gender = '',
    public startDate = dayjs().startOf('year'),
    public endDate = dayjs(),
    public year?: number,
    public month?: string
  ) {
    this.destinationId = destinationId;
    this.departmentId = departmentId;
    this.unitId = unitId;
    this.searchText = searchText;
    this.bloodGroup = bloodGroup;
    this.gender = gender;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  deserialize?(input: any): this {
    Object.assign(this, input);
    return this;
  }
}
