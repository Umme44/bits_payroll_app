import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISalaryCertificate, NewSalaryCertificate } from '../salary-certificate.model';
import { Filter } from '../../../common/employee-address-book/filter.model';
import { ApprovalDTO } from '../../../shared/model/approval-dto.model';
import { IEmployeeSalary } from '../../employee-salary/employee-salary.model';
import { ISalaryCertificateReport } from '../../employment-doc-admin/model/salary-certificate-report.model';

export type PartialUpdateSalaryCertificate = Partial<ISalaryCertificate> & Pick<ISalaryCertificate, 'id'>;

type RestOf<T extends ISalaryCertificate | NewSalaryCertificate> = Omit<T, 'createdAt' | 'updatedAt' | 'sanctionAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
  sanctionAt?: string | null;
};

export type RestSalaryCertificate = RestOf<ISalaryCertificate>;

export type NewRestSalaryCertificate = RestOf<NewSalaryCertificate>;

export type PartialUpdateRestSalaryCertificate = RestOf<PartialUpdateSalaryCertificate>;

export type EntityResponseType = HttpResponse<ISalaryCertificate>;
export type EntityArrayResponseType = HttpResponse<ISalaryCertificate[]>;

@Injectable({ providedIn: 'root' })
export class SalaryCertificateService {
  public resourceUrl = SERVER_API_URL + 'api/payroll-mgt/salary-certificates';
  public resourceUrlApprovalHR = SERVER_API_URL + 'api/payroll-mgt/salary-certificate-approval';

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(salaryCertificate: NewSalaryCertificate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(salaryCertificate);
    return this.http
      .post<RestSalaryCertificate>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(salaryCertificate: ISalaryCertificate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(salaryCertificate);
    return this.http
      .put<RestSalaryCertificate>(`${this.resourceUrl}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSalaryCertificate>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(filter: Filter, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .post<ISalaryCertificate[]>(this.resourceUrl + '/all', filter, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAllPending(): Observable<EntityArrayResponseType> {
    return this.http
      .get<ISalaryCertificate[]>(this.resourceUrlApprovalHR + '/hr', { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  approveSelected(approvalDTO: ApprovalDTO, signatoryPersonId: number): Observable<HttpResponse<boolean>> {
    return this.http.put<boolean>(this.resourceUrlApprovalHR + '/approve-selected-hr/' + signatoryPersonId, approvalDTO, {
      observe: 'response',
    });
  }

  rejectSelected(approvalDTO: ApprovalDTO): Observable<HttpResponse<boolean>> {
    return this.http.put<boolean>(this.resourceUrlApprovalHR + '/reject-selected-hr', approvalDTO, { observe: 'response' });
  }

  getEmployeeListOfSalaries(pin: string): Observable<HttpResponse<IEmployeeSalary[]>> {
    return this.http.post<IEmployeeSalary[]>(this.resourceUrl + '/list-of-salaries/employee', pin, { observe: 'response' });
  }

  getEmployeeSalaryByCertificateId(id: any): Observable<HttpResponse<IEmployeeSalary>> {
    return this.http.get<IEmployeeSalary>(this.resourceUrl + '/employee-salary/' + id, { observe: 'response' });
  }

  getEmployeeSalaryReportByCertificateId(id: any): Observable<HttpResponse<ISalaryCertificateReport>> {
    return this.http
      .get<ISalaryCertificateReport>(this.resourceUrl + '/salary-certificate-report/' + id, { observe: 'response' })
      .pipe(map((res: HttpResponse<ISalaryCertificateReport>) => this.convertDateFromServerForCertificateReport(res)));
  }

  getSalaryCertificateIdentifier(salaryCertificate: Pick<ISalaryCertificate, 'id'>): number {
    return salaryCertificate.id;
  }

  compareSalaryCertificate(o1: Pick<ISalaryCertificate, 'id'> | null, o2: Pick<ISalaryCertificate, 'id'> | null): boolean {
    return o1 && o2 ? this.getSalaryCertificateIdentifier(o1) === this.getSalaryCertificateIdentifier(o2) : o1 === o2;
  }

  addSalaryCertificateToCollectionIfMissing<Type extends Pick<ISalaryCertificate, 'id'>>(
    salaryCertificateCollection: Type[],
    ...salaryCertificatesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const salaryCertificates: Type[] = salaryCertificatesToCheck.filter(isPresent);
    if (salaryCertificates.length > 0) {
      const salaryCertificateCollectionIdentifiers = salaryCertificateCollection.map(
        salaryCertificateItem => this.getSalaryCertificateIdentifier(salaryCertificateItem)!
      );
      const salaryCertificatesToAdd = salaryCertificates.filter(salaryCertificateItem => {
        const salaryCertificateIdentifier = this.getSalaryCertificateIdentifier(salaryCertificateItem);
        if (salaryCertificateCollectionIdentifiers.includes(salaryCertificateIdentifier)) {
          return false;
        }
        salaryCertificateCollectionIdentifiers.push(salaryCertificateIdentifier);
        return true;
      });
      return [...salaryCertificatesToAdd, ...salaryCertificateCollection];
    }
    return salaryCertificateCollection;
  }

  protected convertDateFromClient<T extends ISalaryCertificate | NewSalaryCertificate | PartialUpdateSalaryCertificate>(
    salaryCertificate: T
  ): RestOf<T> {
    return {
      ...salaryCertificate,
      createdAt: salaryCertificate.createdAt?.format(DATE_FORMAT) ?? null,
      updatedAt: salaryCertificate.updatedAt?.format(DATE_FORMAT) ?? null,
      sanctionAt: salaryCertificate.sanctionAt?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restSalaryCertificate: RestSalaryCertificate): ISalaryCertificate {
    return {
      ...restSalaryCertificate,
      createdAt: restSalaryCertificate.createdAt ? dayjs(restSalaryCertificate.createdAt) : undefined,
      updatedAt: restSalaryCertificate.updatedAt ? dayjs(restSalaryCertificate.updatedAt) : undefined,
      sanctionAt: restSalaryCertificate.sanctionAt ? dayjs(restSalaryCertificate.sanctionAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSalaryCertificate>): HttpResponse<ISalaryCertificate> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSalaryCertificate[]>): HttpResponse<ISalaryCertificate[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((salaryCertificate: ISalaryCertificate) => {
        salaryCertificate.createdAt = salaryCertificate.createdAt ? dayjs(salaryCertificate.createdAt) : undefined;
        salaryCertificate.updatedAt = salaryCertificate.updatedAt ? dayjs(salaryCertificate.updatedAt) : undefined;
        salaryCertificate.sanctionAt = salaryCertificate.sanctionAt ? dayjs(salaryCertificate.sanctionAt) : undefined;
      });
    }
    return res;
  }
  protected convertDateFromServerForCertificateReport(res: HttpResponse<ISalaryCertificateReport>): HttpResponse<ISalaryCertificateReport> {
    if (res.body) {
      res.body.joiningDate = res.body.joiningDate ? dayjs(res.body.joiningDate) : undefined;
      res.body.confirmationDate = res.body.confirmationDate ? dayjs(res.body.confirmationDate) : undefined;
    }
    return res;
  }

}
