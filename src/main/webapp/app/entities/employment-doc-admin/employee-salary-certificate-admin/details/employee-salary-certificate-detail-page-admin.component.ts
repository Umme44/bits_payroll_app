import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ISalaryCertificate } from '../../model/salary-certificate.model';

@Component({
  selector: 'jhi-employee-salary-certificate-detail-admin',
  templateUrl: './employee-salary-certificate-detail-page-admin.component.html',
})
export class EmployeeSalaryCertificateDetailAdminComponent implements OnInit {
  salaryCertificate: ISalaryCertificate | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ salaryCertificate }) => (this.salaryCertificate = salaryCertificate));
  }

  previousState(): void {
    window.history.back();
  }
}
