import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import Swal from 'sweetalert2';
import dayJs from 'dayjs/esm'
import { ISalaryCertificateReport } from '../../employment-doc-admin/model/salary-certificate-report.model';
import { SalaryCertificateService } from '../service/salary-certificate.service';
import { EmployeeCategory } from '../../../shared/model/enumerations/employee-category.model';

@Component({
  selector: 'jhi-salary-certificate',
  templateUrl: './salary-certificate-printable.component.html',
  styleUrls: ['salary-certificate-printable.component.scss'],
})
export class SalaryCertificatePrintableComponent implements OnInit {
  salaryCertificateReport!: ISalaryCertificateReport;
  today!: any;

  organizationFullName = '';

  constructor(
    protected activatedRoute: ActivatedRoute,
    private salaryCertificateService: SalaryCertificateService,
    private router: Router
  ) {
    if (sessionStorage.getItem('organizationFullName')) {
      this.organizationFullName = sessionStorage.getItem('organizationFullName')!;
    }
  }

  ngOnInit(): void {
    this.today = dayJs().startOf('day').format('DD MMMM YYYY').toString();

    this.activatedRoute.data.subscribe(({ salaryCertificate }) => {
      if (salaryCertificate.id) {
        if (salaryCertificate.status === 'PENDING') {
          Swal.fire({
            icon: 'error',
            title: 'Oops...',
            text: 'Application in Pending!',
          });
          this.router.navigate(['/salary-certificate']);
        } else if (salaryCertificate.status === 'NOT_APPROVED') {
          Swal.fire({
            icon: 'error',
            title: 'Oops...',
            text: 'Application has not Approved!',
          });
          this.router.navigate(['/salary-certificate']);
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
    if (employeeCategory === EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE) return 'Regular Employee';
    else if (employeeCategory === EmployeeCategory.REGULAR_PROVISIONAL_EMPLOYEE) return 'Regular Provisional Employee';
    else if (employeeCategory === EmployeeCategory.CONTRACTUAL_EMPLOYEE) return 'Regular Provisional Employee';
    else if (employeeCategory === EmployeeCategory.INTERN) return 'Intern';
    else return '';
  }

  print(): void {
    window.print();
  }
}
