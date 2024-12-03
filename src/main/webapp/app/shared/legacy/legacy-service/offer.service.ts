import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';
import { IOffer } from '../legacy-model/offer.model';
import { createRequestOption } from '../../../core/request/request-util';
import { DATE_FORMAT } from '../../../config/input.constants';

type EntityResponseType = HttpResponse<IOffer>;
type EntityArrayResponseType = HttpResponse<IOffer[]>;

@Injectable({ providedIn: 'root' })
export class OfferService {
  public resourceUrl = SERVER_API_URL + 'api/employee-mgt/offers';
  public resourceUrlCommon = SERVER_API_URL + 'api/common/offers';

  constructor(protected http: HttpClient) {}

  create(file: File, offer: IOffer): Observable<EntityResponseType> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    const copy = this.convertDateFromClient(offer);
    formData.append('offer', new Blob([JSON.stringify(copy)], { type: 'application/json' }));
    return this.http
      .post<IOffer>(this.resourceUrl + '/multipart', formData, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(offer: IOffer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(offer);
    return this.http
      .put<IOffer>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  updateWithMultipartData(file: File, offer: IOffer): Observable<EntityResponseType> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    const copy = this.convertDateFromClient(offer);
    formData.append('offer', new Blob([JSON.stringify(copy)], { type: 'application/json' }));
    return this.http
      .put<IOffer>(this.resourceUrl + '/multipart', formData, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IOffer>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IOffer[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  recentList(): Observable<EntityArrayResponseType> {
    return this.http
      .get<IOffer[]>(this.resourceUrlCommon + '/recent-list', { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  archiveList(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IOffer[]>(this.resourceUrlCommon + '/archive-list', { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(offer: IOffer): IOffer {
    const copy: IOffer = Object.assign({}, offer, {
      createdAt: offer.createdAt && offer.createdAt.isValid() ? offer.createdAt.format(DATE_FORMAT) : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdAt = res.body.createdAt ? dayjs(res.body.createdAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((offer: IOffer) => {
        offer.createdAt = offer.createdAt ? dayjs(offer.createdAt) : undefined;
      });
    }
    return res;
  }
}
