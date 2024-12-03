import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITrainingHistory } from '../training-history.model';

@Component({
  selector: 'jhi-training-history-detail',
  templateUrl: './training-history-detail.component.html',
})
export class TrainingHistoryDetailComponent implements OnInit {
  trainingHistory: ITrainingHistory | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ trainingHistory }) => {
      this.trainingHistory = trainingHistory;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
