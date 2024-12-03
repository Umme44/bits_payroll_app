import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPfAccount } from '../pf-account.model';

@Component({
  selector: 'jhi-pf-account-detail',
  templateUrl: './pf-account-detail.component.html',
})
export class PfAccountDetailComponent implements OnInit {
  pfAccount: IPfAccount | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pfAccount }) => {
      this.pfAccount = pfAccount;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
