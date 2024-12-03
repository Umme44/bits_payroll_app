export class UserFilterDTO {
  constructor(public searchText = '', public authorities = '') {
    this.searchText = searchText;
    this.authorities = authorities;
  }

  deserialize(input: any): this {
    Object.assign(this, input);
    return this;
  }
}
