import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEmploymentHistory } from '../employment-history.model';

@Component({
  selector: 'jhi-employment-history-detail',
  templateUrl: './employment-history-detail.component.html',
})
export class EmploymentHistoryDetailComponent implements OnInit {
  employmentHistory: IEmploymentHistory | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employmentHistory }) => {
      this.employmentHistory = employmentHistory;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
