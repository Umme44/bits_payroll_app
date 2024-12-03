import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBankBranch } from '../bank-branch.model';

@Component({
  selector: 'jhi-bank-branch-detail',
  templateUrl: './bank-branch-detail.component.html',
})
export class BankBranchDetailComponent implements OnInit {
  bankBranch: IBankBranch | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bankBranch }) => {
      this.bankBranch = bankBranch;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
