import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';
import { ILocation } from 'app/shared/model/location.model';
import { createRequestOption } from '../../core/request/request-util';

type EntityResponseType = HttpResponse<ILocation>;
type EntityArrayResponseType = HttpResponse<ILocation[]>;

@Injectable({ providedIn: 'root' })
export class LocationService {
  public resourceUrl = SERVER_API_URL + 'api/payroll-mgt/locations';

  constructor(protected http: HttpClient) {}

  create(location: ILocation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(location);
    return this.http
      .post<ILocation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(location: ILocation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(location);
    return this.http
      .put<ILocation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILocation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  getAllNonSuccessorLocations(id: number): Observable<EntityArrayResponseType> {
    return this.http
      .get<ILocation[]>(`${this.resourceUrl}/${id}/non-successor-locations`, { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILocation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(location: ILocation): ILocation {
    const copy: ILocation = Object.assign({}, location, {
      createdAt: location.createdAt && location.createdAt.isValid() ? location.createdAt.toJSON() : undefined,
      updateAt: location.updateAt && location.updateAt.isValid() ? location.updateAt.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdAt = res.body.createdAt ? dayjs(res.body.createdAt) : undefined;
      res.body.updateAt = res.body.updateAt ? dayjs(res.body.updateAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((location: ILocation) => {
        location.createdAt = location.createdAt ? dayjs(location.createdAt) : undefined;
        location.updateAt = location.updateAt ? dayjs(location.updateAt) : undefined;
      });
    }
    return res;
  }
}
