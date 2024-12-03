import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import Swal from 'sweetalert2';
import dayjs from 'dayjs/esm';
import { EmployeeCategory } from '../../../../shared/model/enumerations/employee-category.model';
import { EmployeeSalaryCertificateAdminService } from 'app/entities/employment-doc-admin/employee-salary-certificate-admin/service/employee-salary-certificate-admin.service';
import { ISalaryCertificateReport } from '../../model/salary-certificate-report.model';

@Component({
  selector: 'jhi-employee-salary-certificate-print-format',
  templateUrl: './employee-salary-certificate-print-format.component.html',
  styleUrls: ['./employee-salary-certificate-print-format.component.scss'],
})
export class EmployeeSalaryCertificatePrintFormatComponent implements OnInit {
  salaryCertificateReport!: ISalaryCertificateReport;
  today!: any;

  organizationFullName = '';

  constructor(
    protected activatedRoute: ActivatedRoute,
    private salaryCertificateService: EmployeeSalaryCertificateAdminService,
    private router: Router
  ) {
    if (sessionStorage.getItem('organizationFullName')) {
      this.organizationFullName = sessionStorage.getItem('organizationFullName')!;
    }
  }

  ngOnInit(): void {
    this.today = dayjs().startOf('day');

    this.activatedRoute.data.subscribe(({ salaryCertificate }) => {
      if (salaryCertificate.id) {
        if (salaryCertificate.status === 'PENDING') {
          Swal.fire({
            icon: 'error',
            title: 'Oops...',
            text: 'Application in Pending!',
          });
          this.router.navigate(['/employee-docs-admin']);
        } else if (salaryCertificate.status === 'NOT_APPROVED') {
          Swal.fire({
            icon: 'error',
            title: 'Oops...',
            text: 'Application has not Approved!',
          });
          this.router.navigate(['/employee-docs-admin']);
        } else if (salaryCertificate.status === 'APPROVED') {
          this.salaryCertificateService.getEmployeeSalaryReportByCertificateId(salaryCertificate.id).subscribe(res => {
            this.salaryCertificateReport = res.body!;
          });
        }
      }
    });
  }

  monthNameNormaCapitalize(month: any): string {
    month = month.toString().toLowerCase();
    month = month.charAt(0).toUpperCase() + month.slice(1).toLowerCase();
    return month;
  }

  employeeCategoryCapitalize(employeeCategory: EmployeeCategory): string {
    if (employeeCategory === EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE) return 'regular';
    else if (employeeCategory === EmployeeCategory.REGULAR_PROVISIONAL_EMPLOYEE) return 'regular provisional';
    else if (employeeCategory === EmployeeCategory.CONTRACTUAL_EMPLOYEE) return 'regular provisional';
    else if (employeeCategory === EmployeeCategory.INTERN) return 'intern';
    else return '';
  }

  print(): void {
    window.print();
  }
}
