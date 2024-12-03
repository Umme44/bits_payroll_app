export interface IQuickFilterDto {
  searchText: string;
}

export class QuickFilterDTO implements IQuickFilterDto {
  constructor(public searchText = '') {
    this.searchText = searchText;
  }
}
