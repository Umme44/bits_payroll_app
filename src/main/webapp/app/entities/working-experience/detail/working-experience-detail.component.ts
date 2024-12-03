import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWorkingExperience } from '../working-experience.model';

@Component({
  selector: 'jhi-working-experience-detail',
  templateUrl: './working-experience-detail.component.html',
})
export class WorkingExperienceDetailComponent implements OnInit {
  workingExperience: IWorkingExperience | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workingExperience }) => {
      this.workingExperience = workingExperience;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
