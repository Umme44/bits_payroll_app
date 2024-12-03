import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAitPayment, NewAitPayment } from '../ait-payment.model';

export type PartialUpdateAitPayment = Partial<IAitPayment> & Pick<IAitPayment, 'id'>;

type RestOf<T extends IAitPayment | NewAitPayment> = Omit<T, 'date'> & {
  date?: string | null;
};

export type RestAitPayment = RestOf<IAitPayment>;

export type NewRestAitPayment = RestOf<NewAitPayment>;

export type PartialUpdateRestAitPayment = RestOf<PartialUpdateAitPayment>;

export type EntityResponseType = HttpResponse<IAitPayment>;
export type EntityArrayResponseType = HttpResponse<IAitPayment[]>;

@Injectable({ providedIn: 'root' })
export class AitPaymentService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('/api/payroll-mgt/ait-payments');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(aitPayment: IAitPayment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(aitPayment);
    return this.http
      .post<IAitPayment>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(aitPayment: IAitPayment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(aitPayment);
    return this.http
      .put<IAitPayment>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAitPayment>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAitPayment[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(aitPayment: IAitPayment): IAitPayment {
    const copy: IAitPayment = Object.assign({}, aitPayment, {
      date: aitPayment.date && aitPayment.date.isValid() ? aitPayment.date.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((aitPayment: IAitPayment) => {
        aitPayment.date = aitPayment.date ? dayjs(aitPayment.date) : undefined;
      });
    }
    return res;
  }
}
