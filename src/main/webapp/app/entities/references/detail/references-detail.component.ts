import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IReferences } from '../references.model';

@Component({
  selector: 'jhi-references-detail',
  templateUrl: './references-detail.component.html',
})
export class ReferencesDetailComponent implements OnInit {
  references: IReferences | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ references }) => {
      this.references = references;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
