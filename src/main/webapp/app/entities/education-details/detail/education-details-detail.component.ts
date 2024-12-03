import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEducationDetails } from '../education-details.model';

@Component({
  selector: 'jhi-education-details-detail',
  templateUrl: './education-details-detail.component.html',
})
export class EducationDetailsDetailComponent implements OnInit {
  educationDetails: IEducationDetails | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ educationDetails }) => {
      this.educationDetails = educationDetails;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
