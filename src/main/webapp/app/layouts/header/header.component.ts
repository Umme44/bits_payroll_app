import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent implements OnInit {
  @Input()
  headerName!: string;
  @Input()
  addButtonText!: string;
  @Input()
  routerLinkText!: string;
  @Input()
  shouldHaveHr!: boolean;
  @Input()
  shouldCapitalize = true;

  @Input()
  midHeaderName01!: string;
  @Input()
  midRouterLinkText01!: string;
  @Input()
  midHeader01hasSessionStorage = false;
  @Input()
  midHeader01SessionStorageKey = 'none';
  @Input()
  midHeader01SessionStorageValue = 'none';

  @Input()
  midHeaderName02!: string;
  @Input()
  midRouterLinkText02!: string;
  @Input()
  midHeader02hasSessionStorage = false;
  @Input()
  midHeader02SessionStorageKey = 'none';
  @Input()
  midHeader02SessionStorageValue = 'none';

  @Input()
  midHeaderName03!: string;
  @Input()
  midRouterLinkText03!: string;
  @Input()
  midHeader03hasSessionStorage = false;
  @Input()
  midHeader03SessionStorageKey = 'none';
  @Input()
  midHeader03SessionStorageValue = 'none';

  constructor() {}

  ngOnInit(): void {}

  onClickMidHeader01(): void {
    if (this.midHeader01hasSessionStorage) {
      sessionStorage.setItem(this.midHeader01SessionStorageKey, this.midHeader01SessionStorageValue);
    }
  }

  onClickMidHeader02(): void {
    if (this.midHeader02hasSessionStorage) {
      sessionStorage.setItem(this.midHeader02SessionStorageKey, this.midHeader02SessionStorageValue);
    }
  }
}
