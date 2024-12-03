import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFestivalBonusDetails } from '../festival-bonus-details.model';

@Component({
  selector: 'jhi-festival-bonus-details-detail',
  templateUrl: './festival-bonus-details-detail.component.html',
})
export class FestivalBonusDetailsDetailComponent implements OnInit {
  festivalBonusDetails: IFestivalBonusDetails | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ festivalBonusDetails }) => {
      this.festivalBonusDetails = festivalBonusDetails;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
