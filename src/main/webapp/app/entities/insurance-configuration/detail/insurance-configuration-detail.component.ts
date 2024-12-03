import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IInsuranceConfiguration } from '../insurance-configuration.model';

@Component({
  selector: 'jhi-insurance-configuration-detail',
  templateUrl: './insurance-configuration-detail.component.html',
})
export class InsuranceConfigurationDetailComponent implements OnInit {
  insuranceConfiguration: IInsuranceConfiguration | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ insuranceConfiguration }) => (this.insuranceConfiguration = insuranceConfiguration));
  }

  previousState(): void {
    window.history.back();
  }
}
