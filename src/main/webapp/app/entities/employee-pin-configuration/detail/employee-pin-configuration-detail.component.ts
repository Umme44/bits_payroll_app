import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEmployeePinConfiguration } from '../employee-pin-configuration.model';

@Component({
  selector: 'jhi-employee-pin-configuration-detail',
  templateUrl: './employee-pin-configuration-detail.component.html',
})
export class EmployeePinConfigurationDetailComponent implements OnInit {
  employeePinConfiguration: IEmployeePinConfiguration | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employeePinConfiguration }) => {
      this.employeePinConfiguration = employeePinConfiguration;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
