import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IEmploymentHistory } from '../../../shared/legacy/legacy-model/employment-history.model';

@Component({
  selector: 'jhi-employment-history-detail',
  templateUrl: './increment-detail.component.html',
})
export class IncrementDetailComponent implements OnInit {
  employmentHistory!: IEmploymentHistory;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employmentHistory }) => {
      this.employmentHistory = employmentHistory;

      // tslint:disable-next-line: no-console
      console.log(employmentHistory);
    });
  }

  previousState(): void {
    window.history.back();
  }
}
