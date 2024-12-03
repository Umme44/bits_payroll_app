import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPfNominee } from '../pf-nominee.model';

@Component({
  selector: 'jhi-pf-nominee-detail',
  templateUrl: './pf-nominee-detail.component.html',
})
export class PfNomineeDetailComponent implements OnInit {
  pfNominee: IPfNominee | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pfNominee }) => {
      this.pfNominee = pfNominee;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
