import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProRataFestivalBonus } from '../pro-rata-festival-bonus.model';

export type PartialUpdateProRataFestivalBonus = Partial<IProRataFestivalBonus> & Pick<IProRataFestivalBonus, 'id'>;

type RestOf<T extends IProRataFestivalBonus> = Omit<T, 'date'> & {
  date?: string | null;
};

export type RestProRataFestivalBonus = RestOf<IProRataFestivalBonus>;

// export type NewRestProRataFestivalBonus = RestOf<NewProRataFestivalBonus>;

export type PartialUpdateRestProRataFestivalBonus = RestOf<PartialUpdateProRataFestivalBonus>;

export type EntityResponseType = HttpResponse<IProRataFestivalBonus>;
export type EntityArrayResponseType = HttpResponse<IProRataFestivalBonus[]>;

@Injectable({ providedIn: 'root' })
export class ProRataFestivalBonusService {
  public resourceUrl = SERVER_API_URL + 'api/payroll-mgt/pro-rata-festival-bonuses';

  constructor(protected http: HttpClient) {}

  create(proRataFestivalBonus: IProRataFestivalBonus): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(proRataFestivalBonus);
    return this.http
      .post<IProRataFestivalBonus>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(proRataFestivalBonus: IProRataFestivalBonus): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(proRataFestivalBonus);
    return this.http
      .put<IProRataFestivalBonus>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IProRataFestivalBonus>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IProRataFestivalBonus[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(proRataFestivalBonus: IProRataFestivalBonus): IProRataFestivalBonus {
    const copy: IProRataFestivalBonus = Object.assign({}, proRataFestivalBonus, {
      date: proRataFestivalBonus.date && proRataFestivalBonus.date.isValid() ? proRataFestivalBonus.date.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((proRataFestivalBonus: IProRataFestivalBonus) => {
        proRataFestivalBonus.date = proRataFestivalBonus.date ? dayjs(proRataFestivalBonus.date) : undefined;
      });
    }
    return res;
  }
}
