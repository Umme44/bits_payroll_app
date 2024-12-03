import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';
import { Fraction, IFraction } from '../../shared/model/fraction.model';
import { IEmployeeSalary } from '../../shared/legacy/legacy-model/employee-salary.model';
import { ApplicationConfigService } from '../../core/config/application-config.service';
import { createRequestOption } from '../../core/request/request-util';
import { DATE_FORMAT } from '../../config/input.constants';

type EntityResponseType = HttpResponse<IEmployeeSalary>;
type EntityArrayResponseType = HttpResponse<IEmployeeSalary[]>;
type FractionResponseType = HttpResponse<Fraction[]>;

@Injectable({ providedIn: 'root' })
export class EndUserPayslipService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/common/user-salary');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEmployeeSalary>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEmployeeSalary[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(employeeSalary: IEmployeeSalary): IEmployeeSalary {
    const copy: IEmployeeSalary = Object.assign({}, employeeSalary, {
      salaryGenerationDate:
        employeeSalary.salaryGenerationDate && employeeSalary.salaryGenerationDate.isValid()
          ? employeeSalary.salaryGenerationDate.format(DATE_FORMAT)
          : undefined,
      createdAt: employeeSalary.createdAt && employeeSalary.createdAt.isValid() ? employeeSalary.createdAt.toJSON() : undefined,
      updatedAt: employeeSalary.updatedAt && employeeSalary.updatedAt.isValid() ? employeeSalary.updatedAt.toJSON() : undefined,
      joiningDate:
        employeeSalary.joiningDate && employeeSalary.joiningDate.isValid() ? employeeSalary.joiningDate.format(DATE_FORMAT) : undefined,
      confirmationDate:
        employeeSalary.confirmationDate && employeeSalary.confirmationDate.isValid()
          ? employeeSalary.confirmationDate.format(DATE_FORMAT)
          : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.salaryGenerationDate = res.body.salaryGenerationDate ? dayjs(res.body.salaryGenerationDate) : undefined;
      res.body.createdAt = res.body.createdAt ? dayjs(res.body.createdAt) : undefined;
      res.body.updatedAt = res.body.updatedAt ? dayjs(res.body.updatedAt) : undefined;
      res.body.joiningDate = res.body.joiningDate ? dayjs(res.body.joiningDate) : undefined;
      res.body.confirmationDate = res.body.confirmationDate ? dayjs(res.body.confirmationDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((employeeSalary: IEmployeeSalary) => {
        employeeSalary.salaryGenerationDate = employeeSalary.salaryGenerationDate ? dayjs(employeeSalary.salaryGenerationDate) : undefined;
        employeeSalary.createdAt = employeeSalary.createdAt ? dayjs(employeeSalary.createdAt) : undefined;
        employeeSalary.updatedAt = employeeSalary.updatedAt ? dayjs(employeeSalary.updatedAt) : undefined;
        employeeSalary.joiningDate = employeeSalary.joiningDate ? dayjs(employeeSalary.joiningDate) : undefined;
        employeeSalary.confirmationDate = employeeSalary.confirmationDate ? dayjs(employeeSalary.confirmationDate) : undefined;
      });
    }
    return res;
  }

  protected convertFractionArrayFromServer(res: FractionResponseType): FractionResponseType {
    if (res.body) {
      res.body.forEach((fraction: IFraction) => {
        fraction.startDate = fraction.startDate ? dayjs(fraction.startDate) : undefined;
        fraction.endDate = fraction.endDate ? dayjs(fraction.endDate) : undefined;
      });
    }
    return res;
  }
}
