import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IArrearPayment, NewArrearPayment } from '../arrear-payment.model';

export type PartialUpdateArrearPayment = Partial<IArrearPayment> & Pick<IArrearPayment, 'id'>;

type RestOf<T extends IArrearPayment | NewArrearPayment> = Omit<T, 'disbursementDate'> & {
  disbursementDate?: string | null;
};

export type RestArrearPayment = RestOf<IArrearPayment>;

export type NewRestArrearPayment = RestOf<NewArrearPayment>;

export type PartialUpdateRestArrearPayment = RestOf<PartialUpdateArrearPayment>;

export type EntityResponseType = HttpResponse<IArrearPayment>;
export type EntityArrayResponseType = HttpResponse<IArrearPayment[]>;

@Injectable({ providedIn: 'root' })
export class ArrearPaymentService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/arrear-payments');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(arrearPayment: NewArrearPayment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(arrearPayment);
    return this.http
      .post<RestArrearPayment>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(arrearPayment: IArrearPayment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(arrearPayment);
    return this.http
      .put<RestArrearPayment>(`${this.resourceUrl}/${this.getArrearPaymentIdentifier(arrearPayment)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(arrearPayment: PartialUpdateArrearPayment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(arrearPayment);
    return this.http
      .patch<RestArrearPayment>(`${this.resourceUrl}/${this.getArrearPaymentIdentifier(arrearPayment)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestArrearPayment>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestArrearPayment[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getArrearPaymentIdentifier(arrearPayment: Pick<IArrearPayment, 'id'>): number {
    return arrearPayment.id;
  }

  compareArrearPayment(o1: Pick<IArrearPayment, 'id'> | null, o2: Pick<IArrearPayment, 'id'> | null): boolean {
    return o1 && o2 ? this.getArrearPaymentIdentifier(o1) === this.getArrearPaymentIdentifier(o2) : o1 === o2;
  }

  addArrearPaymentToCollectionIfMissing<Type extends Pick<IArrearPayment, 'id'>>(
    arrearPaymentCollection: Type[],
    ...arrearPaymentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const arrearPayments: Type[] = arrearPaymentsToCheck.filter(isPresent);
    if (arrearPayments.length > 0) {
      const arrearPaymentCollectionIdentifiers = arrearPaymentCollection.map(
        arrearPaymentItem => this.getArrearPaymentIdentifier(arrearPaymentItem)!
      );
      const arrearPaymentsToAdd = arrearPayments.filter(arrearPaymentItem => {
        const arrearPaymentIdentifier = this.getArrearPaymentIdentifier(arrearPaymentItem);
        if (arrearPaymentCollectionIdentifiers.includes(arrearPaymentIdentifier)) {
          return false;
        }
        arrearPaymentCollectionIdentifiers.push(arrearPaymentIdentifier);
        return true;
      });
      return [...arrearPaymentsToAdd, ...arrearPaymentCollection];
    }
    return arrearPaymentCollection;
  }

  protected convertDateFromClient<T extends IArrearPayment | NewArrearPayment | PartialUpdateArrearPayment>(arrearPayment: T): RestOf<T> {
    return {
      ...arrearPayment,
      disbursementDate: arrearPayment.disbursementDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restArrearPayment: RestArrearPayment): IArrearPayment {
    return {
      ...restArrearPayment,
      disbursementDate: restArrearPayment.disbursementDate ? dayjs(restArrearPayment.disbursementDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestArrearPayment>): HttpResponse<IArrearPayment> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestArrearPayment[]>): HttpResponse<IArrearPayment[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
