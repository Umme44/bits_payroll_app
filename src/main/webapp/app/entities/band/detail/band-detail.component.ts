import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBand } from '../band.model';

@Component({
  selector: 'jhi-band-detail',
  templateUrl: './band-detail.component.html',
})
export class BandDetailComponent implements OnInit {
  band: IBand | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ band }) => {
      this.band = band;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
