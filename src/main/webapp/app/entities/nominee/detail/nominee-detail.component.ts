import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { INominee } from '../nominee.model';

@Component({
  selector: 'jhi-nominee-detail',
  templateUrl: './nominee-detail.component.html',
})
export class NomineeDetailComponent implements OnInit {
  nominee: INominee | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ nominee }) => {
      this.nominee = nominee;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
