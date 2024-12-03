import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFestivalBonusConfig } from '../festival-bonus-config.model';

@Component({
  selector: 'jhi-festival-bonus-config-detail',
  templateUrl: './festival-bonus-config-detail.component.html',
})
export class FestivalBonusConfigDetailComponent implements OnInit {
  festivalBonusConfig: IFestivalBonusConfig | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ festivalBonusConfig }) => {
      this.festivalBonusConfig = festivalBonusConfig;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
