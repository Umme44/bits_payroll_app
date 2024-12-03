import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';
import { IEmploymentHistory } from '../legacy-model/employment-history.model';
import { ApplicationConfigService } from '../../../core/config/application-config.service';
import { createRequestOption } from '../../../core/request/request-util';
import { DATE_FORMAT } from '../../../config/input.constants';

type EntityResponseType = HttpResponse<IEmploymentHistory>;
type EntityArrayResponseType = HttpResponse<IEmploymentHistory[]>;

@Injectable({ providedIn: 'root' })
export class EmploymentHistoryService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/employee-mgt/employment-histories');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(employmentHistory: IEmploymentHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employmentHistory);
    return this.http
      .post<IEmploymentHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(employmentHistory: IEmploymentHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employmentHistory);
    return this.http
      .put<IEmploymentHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEmploymentHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEmploymentHistory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(employmentHistory: IEmploymentHistory): IEmploymentHistory {
    const copy: IEmploymentHistory = Object.assign({}, employmentHistory, {
      effectiveDate:
        employmentHistory.effectiveDate && employmentHistory.effectiveDate.isValid()
          ? employmentHistory.effectiveDate.format(DATE_FORMAT)
          : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.effectiveDate = res.body.effectiveDate ? dayjs(res.body.effectiveDate) : undefined;
      res.body.dateOfJoining = res.body.dateOfJoining ? dayjs(res.body.dateOfJoining) : undefined;
      res.body.dateOfConfirmation = res.body.dateOfConfirmation ? dayjs(res.body.dateOfConfirmation) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((employmentHistory: IEmploymentHistory) => {
        employmentHistory.effectiveDate = employmentHistory.effectiveDate ? dayjs(employmentHistory.effectiveDate) : undefined;
      });
    }
    return res;
  }

  protected convertDateArrayFromClient(employmentHistories: IEmploymentHistory[]): IEmploymentHistory[] {
    const convertedEmploymentHistories: IEmploymentHistory[] = [];

    for (const employmentHistory of employmentHistories) {
      const copy: IEmploymentHistory = Object.assign({}, employmentHistory, {
        effectiveDate:
          employmentHistory.effectiveDate && employmentHistory.effectiveDate.isValid()
            ? employmentHistory.effectiveDate.format(DATE_FORMAT)
            : undefined,
      });
      convertedEmploymentHistories.push(copy);
    }

    return convertedEmploymentHistories;
  }
}
