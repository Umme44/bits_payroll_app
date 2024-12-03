import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPfCollection } from '../pf-collection.model';

@Component({
  selector: 'jhi-pf-collection-detail',
  templateUrl: './pf-collection-detail.component.html',
})
export class PfCollectionDetailComponent implements OnInit {
  pfCollection: IPfCollection | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pfCollection }) => {
      this.pfCollection = pfCollection;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
