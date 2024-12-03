import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IEmploymentCertificate } from '../../model/employment-certificate.model';

@Component({
  selector: 'jhi-employment-certificate-print-format',
  templateUrl: './employment-certificate-print-format.component.html',
  styleUrls: ['./employment-certificate-print-format.component.scss'],
})
export class EmploymentCertificatePrintFormatComponent implements OnInit {
  employmentCertificate: IEmploymentCertificate | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employmentCertificate }) => (this.employmentCertificate = employmentCertificate));
  }

  previousState(): void {
    window.history.back();
  }

  print(): void {
    window.print();
  }
}
