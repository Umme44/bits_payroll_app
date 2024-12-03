import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEmploymentCertificate } from '../employment-certificate.model';

@Component({
  selector: 'jhi-employment-certificate-detail',
  templateUrl: './employment-certificate-detail.component.html',
})
export class EmploymentCertificateDetailComponent implements OnInit {
  employmentCertificate: IEmploymentCertificate | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employmentCertificate }) => {
      this.employmentCertificate = employmentCertificate;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
