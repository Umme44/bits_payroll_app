import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IEmploymentCertificate } from '../../model/employment-certificate.model';

@Component({
  selector: 'jhi-employment-certificate-detail-admin',
  templateUrl: './employment-certificate-detail-admin.component.html',
})
export class EmploymentCertificateDetailAdminComponent implements OnInit {
  employmentCertificate: IEmploymentCertificate | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employmentCertificate }) => (this.employmentCertificate = employmentCertificate));
  }

  previousState(): void {
    window.history.back();
  }
}
