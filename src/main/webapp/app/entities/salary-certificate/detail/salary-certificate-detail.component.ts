import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISalaryCertificate } from '../salary-certificate.model';

@Component({
  selector: 'jhi-salary-certificate-detail',
  templateUrl: './salary-certificate-detail.component.html',
})
export class SalaryCertificateDetailComponent implements OnInit {
  salaryCertificate: ISalaryCertificate | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ salaryCertificate }) => {
      this.salaryCertificate = salaryCertificate;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
