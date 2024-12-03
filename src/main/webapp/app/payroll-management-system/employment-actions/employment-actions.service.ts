import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { createRequestOption } from '../../core/request/request-util';
import dayjs from 'dayjs/esm';
import { ApplicationConfigService } from '../../core/config/application-config.service';
import { DATE_FORMAT } from '../../config/input.constants';
import { IEmploymentHistory } from '../../shared/legacy/legacy-model/employment-history.model';

type EntityResponseType = HttpResponse<IEmploymentHistory>;
type EntityArrayResponseType = HttpResponse<IEmploymentHistory[]>;

@Injectable({ providedIn: 'root' })
export class EmploymentActionsService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/employee-mgt');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  // increment
  createIncrement(employmentHistory: IEmploymentHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employmentHistory);
    return this.http
      .post<IEmploymentHistory>(this.resourceUrl + '/increment', copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  updateIncrement(employmentHistory: IEmploymentHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employmentHistory);
    return this.http
      .put<IEmploymentHistory>(this.resourceUrl + '/increment', copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  findIncrement(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEmploymentHistory>(this.resourceUrl + '/increment' + '/' + id, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  queryIncrement(req?: any): Observable<EntityArrayResponseType> {
    if (req) Object.keys(req).forEach(k => req[k] == null && delete req[k]); // remove null or undefined property
    const options = createRequestOption(req);
    return this.http
      .get<IEmploymentHistory[]>(this.resourceUrl + '/increment', { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  deleteIncrement(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl + '/increment'}/${id}`, { observe: 'response' });
  }

  // transfer
  createTransfer(employmentHistory: IEmploymentHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employmentHistory);
    return this.http
      .post<IEmploymentHistory>(this.resourceUrl + '/transfer', copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  updateTransfer(employmentHistory: IEmploymentHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employmentHistory);
    return this.http
      .put<IEmploymentHistory>(this.resourceUrl + '/transfer', copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  findTransfer(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEmploymentHistory>(this.resourceUrl + '/transfer' + '/' + id, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  queryTransfer(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEmploymentHistory[]>(this.resourceUrl + '/transfer', { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  queryTransferByIdAndDate(
    employeeId: Number,
    startdate: dayjs.Dayjs,
    enddate: dayjs.Dayjs,
    req?: any
  ): Observable<EntityArrayResponseType> {
    const startDate: string = startdate.format('YYYY-MM-DD');
    const endDate: string = enddate.format('YYYY-MM-DD');
    const url = this.resourceUrl + '/transfer-search/' + employeeId + '/' + startDate + '/' + endDate;

    const options = createRequestOption(req);
    return this.http
      .get<IEmploymentHistory[]>(url, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  queryTransferwithdate(startdate: dayjs.Dayjs, enddate: dayjs.Dayjs, req?: any): Observable<EntityArrayResponseType> {
    const startDate: string = startdate.format('YYYY-MM-DD');
    const endDate: string = enddate.format('YYYY-MM-DD');
    const url = this.resourceUrl + '/transfer-search/' + startDate + '/' + endDate;

    const options = createRequestOption(req);
    return this.http
      .get<IEmploymentHistory[]>(url, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  queryTransferwithemployeeId(employeeId: Number, req?: any): Observable<EntityArrayResponseType> {
    const url = this.resourceUrl + '/transfer-search/' + employeeId;

    const options = createRequestOption(req);
    return this.http
      .get<IEmploymentHistory[]>(url, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  deleteTransfer(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl + '/transfer'}/${id}`, { observe: 'response' });
  }

  // promotions
  createPromotions(employmentHistory: IEmploymentHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employmentHistory);
    return this.http
      .post<IEmploymentHistory>(this.resourceUrl + '/promotion', copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  updatePromotions(employmentHistory: IEmploymentHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employmentHistory);
    return this.http
      .put<IEmploymentHistory>(this.resourceUrl + '/promotion', copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  findPromotions(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEmploymentHistory>(this.resourceUrl + '/promotion' + '/' + id, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  queryPromotions(req?: any): Observable<EntityArrayResponseType> {
    if (req) Object.keys(req).forEach(k => req[k] == null && delete req[k]); // remove null or undefined property
    const options = createRequestOption(req);
    return this.http
      .get<IEmploymentHistory[]>(this.resourceUrl + '/promotion', { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  deletePromotions(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl + '/promotion'}/${id}`, { observe: 'response' });
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
}
