import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { IBand } from '../legacy-model/band.model';
import { ApplicationConfigService } from '../../../core/config/application-config.service';
import { DATE_FORMAT } from '../../../config/input.constants';
import dayjs from 'dayjs/esm';
import { createRequestOption } from '../../../core/request/request-util';

type EntityResponseType = HttpResponse<IBand>;
type EntityArrayResponseType = HttpResponse<IBand[]>;

@Injectable({ providedIn: 'root' })
export class BandService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/employee-mgt/bands');
  public resourceUrlCommon = this.applicationConfigService.getEndpointFor('api/common/bands');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(band: IBand): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(band);
    return this.http
      .post<IBand>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(band: IBand): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(band);
    return this.http
      .put<IBand>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IBand>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IBand[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  findAllForCommon(): Observable<EntityArrayResponseType> {
    return this.http
      .get<IBand[]>(this.resourceUrlCommon, { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(band: IBand): IBand {
    const copy: IBand = Object.assign({}, band, {
      createdAt: band.createdAt && band.createdAt.isValid() ? band.createdAt.format(DATE_FORMAT) : undefined,
      updatedAt: band.updatedAt && band.updatedAt.isValid() ? band.updatedAt.format(DATE_FORMAT) : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdAt = res.body.createdAt ? dayjs(res.body.createdAt) : undefined;
      res.body.updatedAt = res.body.updatedAt ? dayjs(res.body.updatedAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((band: IBand) => {
        band.createdAt = band.createdAt ? dayjs(band.createdAt) : undefined;
        band.updatedAt = band.updatedAt ? dayjs(band.updatedAt) : undefined;
      });
    }
    return res;
  }
}
