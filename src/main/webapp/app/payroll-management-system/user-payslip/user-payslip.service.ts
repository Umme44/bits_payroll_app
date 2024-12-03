import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';
import { ISelectableDTO } from 'app/shared/model/selectable-dto.model';
import { ISalaryPayslip } from 'app/shared/model/salary-payslip.model';
import { ApplicationConfigService } from '../../core/config/application-config.service';
import { IEmployeeSalary } from '../../shared/legacy/legacy-model/employee-salary.model';
import { IFestivalBonusDetails } from '../../shared/legacy/legacy-model/festival-bonus-details.model';
import { IIndividualArrearPayslip } from '../../shared/model/individual-arrear-payslip.model';

type EntityResponseType = HttpResponse<IEmployeeSalary>;
type EntityResponseTypePaySlip = HttpResponse<ISalaryPayslip>;

@Injectable({
  providedIn: 'root',
})
export class UserPayslipService {
  private resourceUrl = this.applicationConfigService.getEndpointFor('api/common/payslip');
  private resourceUrlFestivalBonus = this.applicationConfigService.getEndpointFor('api/common/festival-bonus-payslip');

  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  getMyDisbursedSalaryYearList(): Observable<number[]> {
    return this.http.get<number[]>(this.applicationConfigService.getEndpointFor('api/common/my-salary-year-list'));
  }

  getYearWiseMyDisbursedSalaryMonthList(year: number): Observable<ISelectableDTO[]> {
    return this.http.get<ISelectableDTO[]>(this.applicationConfigService.getEndpointFor('api/common/my-salary-month-list/' + year));
  }

  getPayslipForYearMonth(year: number, month: number): Observable<EntityResponseTypePaySlip> {
    return this.http
      .get<ISalaryPayslip>(`${this.resourceUrl}/${year}/${month}`, { observe: 'response' })
      .pipe(map((res: EntityResponseTypePaySlip) => this.convertDataFromServerForPaySlip(res)));
  }

  getMyFestivalYearList(): Observable<number[]> {
    return this.http.get<number[]>(this.applicationConfigService.getEndpointFor('/api/common/my-festival-bonus-year-list'));
  }

  getYearWiseFestivalDetailsList(year: number): Observable<IFestivalBonusDetails[]> {
    return this.http.get<IFestivalBonusDetails[]>(
      this.applicationConfigService.getEndpointFor('/api/common/year-wise-festival-bonus-list/' + year)
    );
  }

  protected convertDataFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.salaryGenerationDate = res.body.salaryGenerationDate ? dayjs(res.body.salaryGenerationDate) : undefined;
      res.body.createdAt = res.body.createdAt ? dayjs(res.body.createdAt) : undefined;
      res.body.updatedAt = res.body.updatedAt ? dayjs(res.body.updatedAt) : undefined;
      res.body.joiningDate = res.body.joiningDate ? dayjs(res.body.joiningDate) : undefined;
      res.body.confirmationDate = res.body.confirmationDate ? dayjs(res.body.confirmationDate) : undefined;
    }
    return res;
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
    }
    return res;
  }

  getMyArrearYearList(): Observable<number[]> {
    return this.http.get<number[]>(SERVER_API_URL + '/api/common/arrear-salary-years');
  }

  getMyArrearTitleListByYear(year: number): Observable<ISelectableDTO[]> {
    return this.http.get<ISelectableDTO[]>(SERVER_API_URL + '/api/common/arrear-salary-title/' + year);
  }

  getMyArrearDetail(title: string): Observable<IIndividualArrearPayslip> {
    const uri = SERVER_API_URL + '/api/common/arrear-salary-payslip?title=' + encodeURIComponent(String(title));
    return this.http.get<IIndividualArrearPayslip>(uri);
  }
}
