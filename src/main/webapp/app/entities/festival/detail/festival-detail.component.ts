import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {IFestival} from "../festival.model";


@Component({
  selector: 'jhi-festival-detail',
  templateUrl: './festival-detail.component.html',
})
export class FestivalDetailComponent implements OnInit {
  festival: IFestival | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ festival }) => (this.festival = festival));
  }

  previousState(): void {
    window.history.back();
  }
}
