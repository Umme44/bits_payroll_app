import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';
import { IHoldSalaryDisbursement } from '../legacy-model/hold-salary-disbursement.model';
import { ApplicationConfigService } from '../../../core/config/application-config.service';
import { createRequestOption } from '../../../core/request/request-util';
import { DATE_FORMAT } from '../../../config/input.constants';

type EntityResponseType = HttpResponse<IHoldSalaryDisbursement>;
type EntityArrayResponseType = HttpResponse<IHoldSalaryDisbursement[]>;

@Injectable({ providedIn: 'root' })
export class HoldSalaryDisbursementService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/payroll-mgt/hold-salary-disbursements');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(holdSalaryDisbursement: IHoldSalaryDisbursement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(holdSalaryDisbursement);
    return this.http
      .post<IHoldSalaryDisbursement>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(holdSalaryDisbursement: IHoldSalaryDisbursement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(holdSalaryDisbursement);
    return this.http
      .put<IHoldSalaryDisbursement>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IHoldSalaryDisbursement>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IHoldSalaryDisbursement[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(holdSalaryDisbursement: IHoldSalaryDisbursement): IHoldSalaryDisbursement {
    const copy: IHoldSalaryDisbursement = Object.assign({}, holdSalaryDisbursement, {
      date:
        holdSalaryDisbursement.date && holdSalaryDisbursement.date.isValid() ? holdSalaryDisbursement.date.format(DATE_FORMAT) : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((holdSalaryDisbursement: IHoldSalaryDisbursement) => {
        holdSalaryDisbursement.date = holdSalaryDisbursement.date ? dayjs(holdSalaryDisbursement.date) : undefined;
      });
    }
    return res;
  }
}
