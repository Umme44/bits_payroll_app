import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ISelectableDTO } from 'app/shared/model/selectable-dto.model';
import { ISalaryPayslip } from 'app/shared/model/salary-payslip.model';
import { IEmployeeMinimal } from 'app/shared/model/employee-minimal.model';
import { ApplicationConfigService } from '../../core/config/application-config.service';
import dayjs from 'dayjs/esm';

type EntityResponseTypePaySlip = HttpResponse<ISalaryPayslip>;
type EntityArrayResponseType = HttpResponse<IEmployeeMinimal[]>

@Injectable({
  providedIn: 'root',
})
export class SalaryPayslipAdminService {
  baseUrl = this.applicationConfigService.getEndpointFor('');

  constructor(private http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  getMyDisbursedSalaryYearList(employeeId: number): Observable<number[]> {
    return this.http.get<number[]>(this.baseUrl + `api/payroll-mgt/my-salary-year-list/${employeeId}`);
  }

  getYearWiseMyDisbursedSalaryMonthList(employeeId: number, year: number): Observable<ISelectableDTO[]> {
    return this.http.get<ISelectableDTO[]>(this.baseUrl + `api/payroll-mgt/my-salary-month-list/${employeeId}/${year}`);
  }

  //Here we are fetching data from Employee, but we should fetch data from Custom-Employee
  getPayslipForYearMonth(employeeId: number, year: number, month: number): Observable<EntityResponseTypePaySlip> {
    return this.http
      .get<ISalaryPayslip>(this.baseUrl + `api/payroll-mgt/payslip/${employeeId}/${year}/${month}`, { observe: 'response' })
      .pipe(map((res: EntityResponseTypePaySlip) => this.convertDataFromServerForPaySlip(res)));
  }

  getAllEligibleEmployeeForSalaryPayslip(): Observable<HttpResponse<IEmployeeMinimal[]>> {
    return this.http.get<IEmployeeMinimal[]>(this.baseUrl + `api/payroll-mgt/payslip/eligible-employee`, { observe: 'response' });
  }

  protected convertDataFromServerForPaySlip(res: EntityResponseTypePaySlip): EntityResponseTypePaySlip {
    if (res.body) {
      res.body.employeeSalary!.salaryGenerationDate = res.body.employeeSalary!.salaryGenerationDate
        ? dayjs(res.body.employeeSalary!.salaryGenerationDate)
        : undefined;
      res.body.employeeSalary!.createdAt = res.body.employeeSalary!.createdAt ? dayjs(res.body.employeeSalary!.createdAt) : undefined;
      res.body.employeeSalary!.updatedAt = res.body.employeeSalary!.updatedAt ? dayjs(res.body.employeeSalary!.updatedAt) : undefined;
      res.body.employeeSalary!.joiningDate = res.body.employeeSalary!.joiningDate ? dayjs(res.body.employeeSalary!.joiningDate) : undefined;
      res.body.employeeSalary!.confirmationDate = res.body.employeeSalary!.confirmationDate
        ? dayjs(res.body.employeeSalary!.confirmationDate)
        : undefined;
      res.body.employee!.dateOfJoining = res.body.employee!.dateOfJoining ? dayjs(res.body.employee!.dateOfJoining) : undefined;
    }
    return res;
  }
}
