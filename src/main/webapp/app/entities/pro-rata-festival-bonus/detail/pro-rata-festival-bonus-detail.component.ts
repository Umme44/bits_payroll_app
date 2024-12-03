import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProRataFestivalBonus } from '../pro-rata-festival-bonus.model';

@Component({
  selector: 'jhi-pro-rata-festival-bonus-detail',
  templateUrl: './pro-rata-festival-bonus-detail.component.html',
})
export class ProRataFestivalBonusDetailComponent implements OnInit {
  proRataFestivalBonus: IProRataFestivalBonus | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ proRataFestivalBonus }) => (this.proRataFestivalBonus = proRataFestivalBonus));
  }

  previousState(): void {
    window.history.back();
  }
}
