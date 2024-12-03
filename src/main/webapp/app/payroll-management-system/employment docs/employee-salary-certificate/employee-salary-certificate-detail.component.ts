import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ISalaryCertificate } from '../../../shared/legacy/legacy-model/salary-certificate.model';

@Component({
  selector: 'jhi-employee-salary-certificate-detail',
  templateUrl: './employee-salary-certificate-detail.component.html',
})
export class EmployeeSalaryCertificateDetailComponent implements OnInit {
  salaryCertificate: ISalaryCertificate | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ salaryCertificate }) => (this.salaryCertificate = salaryCertificate));
  }

  previousState(): void {
    window.history.back();
  }
}
