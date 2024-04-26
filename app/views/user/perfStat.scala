package views.html.user

import play.api.i18n.Lang

import lila.app.templating.Environment.{ *, given }

import lila.perfStat.{ PerfStat, PerfStatData }

import lila.rating.PerfType
import lila.core.data.SafeJsonStr

object perfStat:

  lazy val ui = lila.perfStat.PerfStatUi(helpers)

  import trans.perfStat.*

  def apply(data: PerfStatData, ratingChart: Option[SafeJsonStr])(using PageContext) =
    import data.*
    import stat.perfType
    views.html.base.layout(
      title = s"${user.username} - ${perfStats.txt(perfType.trans)}",
      robots = false,
      modules = jsModule("bits.user") ++
        ratingChart.map { rc =>
          jsModuleInit(
            "chart.ratingHistory",
            SafeJsonStr(s"{data:$rc,singlePerfName:'${perfType.trans(using ctxTrans.translator.toDefault)}'}")
          ).some
        },
      moreCss = cssTag("perf-stat")
    ):
      main(cls := s"page-menu")(
        st.aside(cls := "page-menu__menu")(show.page.side(user, ranks, perfType.key.some)),
        div(cls := s"page-menu__content box perf-stat ${perfType.key}")(
          boxTop(
            div(cls := "box__top__title")(
              bits.perfTrophies(user, ranks.view.filterKeys(perfType == _).toMap),
              h1(
                a(href := routes.User.show(user.username))(user.username),
                span(perfStats(perfType.trans))
              )
            ),
            div(cls := "box__top__actions")(
              a(
                cls      := "button button-empty text",
                dataIcon := perfType.icon,
                href     := s"${routes.User.games(user.username, "search")}?perf=${perfType.id}"
              )(viewTheGames())
            )
          ),
          ratingChart.isDefined.option(ui.ratingHistoryContainer),
          ui.content(data)
        )
      )
